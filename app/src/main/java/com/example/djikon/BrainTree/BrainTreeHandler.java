package com.example.djikon.BrainTree;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;

import okhttp3.Route;

public class BrainTreeHandler {


    private static BraintreeGateway gateway = new BraintreeGateway(
            Environment.SANDBOX,
            "5xrr5fzzm2j9wpt5",
            "pbh8534wdgvfwvx2",
            "90beaf12a6ed65f8708e44154203611e"
    );

   /* get(new Route("/client_token") {
        @Override
        public Object handle(Request request, Response response) {
            return gateway.clientToken().generate();
        }
    });*/
}
