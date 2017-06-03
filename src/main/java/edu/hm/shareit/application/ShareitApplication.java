package edu.hm.shareit.application;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import edu.hm.shareit.observer.ShareitServletContextListener;

import javax.inject.Inject;

/**
 * Application class to enable guice within jersey.
 */
class ShareItApplication extends ResourceConfig {
    @Inject
    public ShareItApplication(ServiceLocator serviceLocator) {
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
        GuiceIntoHK2Bridge guiceBridge
                = serviceLocator.getService(GuiceIntoHK2Bridge.class);
        guiceBridge.bridgeGuiceInjector(
                ShareitServletContextListener.getInjectorInstance());
    }
}
