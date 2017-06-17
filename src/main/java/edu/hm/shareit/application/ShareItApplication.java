package edu.hm.shareit.application;

import edu.hm.shareit.listener.ShareitServletContextListener;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;


import javax.inject.Inject;

/**
 * Application class to enable guice within jersey.
 */
public class ShareItApplication extends ResourceConfig {

    @Inject
    public ShareItApplication(ServiceLocator serviceLocator) {
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
        GuiceIntoHK2Bridge guiceBridge
                = serviceLocator.getService(GuiceIntoHK2Bridge.class);
         guiceBridge.bridgeGuiceInjector(
                ShareitServletContextListener.getInjectorInstance());
    }
}