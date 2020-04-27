package io.order;

import io.sentry.core.Sentry;
//import io.sentry.Sentry;
import java.util.*;

public class App
{
    class Request
    {
        public Map<String, Object> Data = new HashMap<String, Object>();
    }

    class Response
    {
        private final String statusCode;
        public String getStatusCode() { return statusCode; }

        Response(String statusCode) { this.statusCode = statusCode; }
    }

    class Purchase
    {
        private final PurchaseItem[] items;
        public Purchase(PurchaseItem[] items) {
            this.items = items;
        }

        public PurchaseItem[] getItems() {
            return items;
        }
    }

    class PurchaseItem
    {
        public final String id;

        public String getId() {
            return id;
        }

        public PurchaseItem(Object cart) {
            this.id = (String)cart;
        }
    }

    class CheckoutService
    {
        public Purchase ConfirmPurchase(Object cart) {
            return new Purchase(new PurchaseItem[] { new PurchaseItem(cart) });
        }

        public Invoice CreateInvoice(Purchase purchase) {
            return new Invoice();
        }
    }

    class Invoice
    {
    }

    class ShippingService
    {
        public void AddOrder(Purchase purchase, Invoice invoice) throws OrderException {
            validatePurchase(purchase);
        }

        public void validatePurchase(Purchase purchase) {
            if (purchase == null) throw new IllegalArgumentException("Purchase is null.");
            if (purchase.getItems() == null) throw new IllegalArgumentException("Purchase items are null.");
            for (PurchaseItem purchaseItem: purchase.getItems())
            {
                if (purchaseItem.getId() == null)
                {
                    throw new IllegalStateException("Invalid purchase item without identifier.");
                }
            }
        }
    }

    class OrderException extends Exception {}

    private static final App app = new App();

    public static void main( String[] args )
    {
        Sentry.init(o -> {
            o.setCacheDirPath("~/test");
            o.setDsn("https://46fee3fb0e2a45cca85f2f2c41efe52c@o117736.ingest.sentry.io/1379099");
        });

        Request request = app.new Request();
        CheckoutService checkoutService = app.new CheckoutService();
        ShippingService shippingService = app.new ShippingService();

        Response response = handleRequest(checkoutService, shippingService, request);
        assert response.statusCode == "200";
    }

    static Response handleRequest(
            CheckoutService checkoutService,
            ShippingService shippingService,
            Request request)
    {
        try
        {
            Object cart = request.Data.get("cart");
            Purchase purchase = checkoutService.ConfirmPurchase(cart);
            Invoice invoice = checkoutService.CreateInvoice(purchase);
            shippingService.AddOrder(purchase, invoice);
            return app.new Response("200");
        }
        catch (OrderException e)
        {
            return app.new Response("400");
        }
    }
}
