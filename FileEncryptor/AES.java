import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    private SecretKey key;
    private final int KEY_SIZE = 128;
    private final int T_LEN = 128;
    private byte[] IV;

    public void init() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(KEY_SIZE);
        key = generator.generateKey();
    }

    public void initFromStrings(String secretKey, String IV) throws Exception {
        try {
            key = new SecretKeySpec(decode(secretKey), "AES");
            this.IV = decode(IV);
        } catch (IllegalArgumentException e) {
            throw new Exception("Invalid Key or IV format. Ensure they are Base64-encoded.");
        }
    }

    public String encrypt(String message) throws Exception {
        byte[] messageInBytes = message.getBytes();
        Cipher encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        encryptionCipher.init(Cipher.ENCRYPT_MODE, key);
        IV = encryptionCipher.getIV();
        byte[] encryptedBytes = encryptionCipher.doFinal(messageInBytes);
        return encode(encryptedBytes);
    }

    public String decrypt(String encryptedMessage) throws Exception {
        byte[] messageInBytes = decode(encryptedMessage);
        Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(T_LEN, IV);
        decryptionCipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] decryptedBytes = decryptionCipher.doFinal(messageInBytes);
        return new String(decryptedBytes);
    }

    private String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    private void saveMessageToFile(String encryptedMessage) throws IOException {
        // Get the current date
        LocalDate currentDate = LocalDate.now();
        String date = currentDate.getDayOfMonth() + "-" + currentDate.getMonthValue() + "-" + currentDate.getYear();

        // Write the encrypted message, key, and IV to the file
        try (FileWriter writer = new FileWriter("message.txt", true)) { // Append mode
            writer.write("Encrypted Message: <" + encryptedMessage + ">\n");
            writer.write("Key: <" + encode(key.getEncoded()) + ">\n");
            writer.write("IV: <" + encode(IV) + ">\n");
            writer.write("Date: " + date + "\n\n");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AES aes = new AES();

        try {
            System.out.println("Welcome to AES Encryption/Decryption!");
            while (true) {
                System.out.println("\nChoose an option:");
                System.out.println("1. Encrypt a message");
                System.out.println("2. Decrypt a message");
                System.out.println("3. Exit");
                System.out.print("Enter your choice (1, 2, or 3): ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                if (choice == 1) {
                    // Encrypt a message
                    System.out.print("Enter the message to encrypt: ");
                    String message = scanner.nextLine();

                    aes.init(); // Generate a new key and IV
                    String encryptedMessage = aes.encrypt(message);

                    // Save the encrypted message, key, and IV to a file
                    aes.saveMessageToFile(encryptedMessage);

                    System.out.println("Encrypted Message: " + encryptedMessage);
                    System.out.println("Message, Key, and IV saved to 'message.txt'.");
                    System.out.println("For decryption, open 'message.txt' to find the Key and IV.");
                } else if (choice == 2) {
                    // Decrypt a message
                    System.out.print("Enter the encrypted message (Base64-encoded): ");
                    String encryptedMessage = scanner.nextLine();

                    System.out.print("Enter the Key (Base64-encoded): ");
                    String secretKey = scanner.nextLine();

                    System.out.print("Enter the IV (Base64-encoded): ");
                    String iv = scanner.nextLine();

                    aes.initFromStrings(secretKey, iv); // Initialize with the provided key and IV

                    String decryptedMessage = aes.decrypt(encryptedMessage);
                    System.out.println("Decrypted Message: " + decryptedMessage);
                } else if (choice == 3) {
                    // Exit the program
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                } else {
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading or writing to file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid Base64 input: " + e.getMessage());
        } catch (javax.crypto.AEADBadTagException e) {
            System.err.println("Decryption failed: Tag mismatch. Ensure the Key, IV, and encrypted message are correct.");
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}