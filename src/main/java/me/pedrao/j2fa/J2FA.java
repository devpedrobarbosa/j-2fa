package me.pedrao.j2fa;

import lombok.Getter;
import me.pedrao.j2fa.authenticator.Authenticator;

import java.util.logging.Logger;

@Getter
public class J2FA {

    private final Logger logger;
    private final String applicationName;
    private final Authenticator authenticator;

    public J2FA(String applicationName) {
        logger = Logger.getLogger("J2FA");
        this.applicationName = applicationName;
        authenticator = new Authenticator(this);
    }
}