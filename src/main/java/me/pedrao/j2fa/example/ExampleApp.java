package me.pedrao.j2fa.example;

import me.pedrao.j2fa.J2FA;

import java.io.File;
import java.util.Scanner;

public class ExampleApp {

    // To run this application, build the project with Gradle and run java -jar ./build/libs/j-2fa-X.X-SNAPSHOT.jar in your project folder
    public static void main(String[] args) {
        // Instantiate J2FA
        final J2FA authenticator = new J2FA("Example App");
        
        // Generate a new Secret Key for each new user
        final String secret = authenticator.generateSecret();
        System.out.println("Welcome, new user!\nYour Secret Key: " + secret +
                "\nAdd it in your Google Authenticator app to get a Token.");
        authenticator.createQRCodeFile(secret, new File("C:/J2FA/qrcode.png").getPath());
        
        // This key should be stored in a database within the user, for future authentications
        System.out.println("Current app Token: ");
        final String token = new Scanner(System.in).nextLine();
        if(authenticator.authenticate(token, secret))
            System.out.println("Access granted!");
        else System.out.println("Access denied. Invalid token.");
    }
}