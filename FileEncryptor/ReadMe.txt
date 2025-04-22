# AES File Encryptor

This is a Java-based application that provides AES encryption and decryption functionality. It allows users to securely encrypt messages and save the encrypted data, along with the encryption key and IV, to a file. The application also supports decrypting messages using the provided key and IV.

## Features

- **AES Encryption**: Encrypts messages using AES with GCM mode for authenticated encryption.
- **AES Decryption**: Decrypts messages using the provided key and IV.
- **File Storage**: Saves the encrypted message, key, and IV to a file (`message.txt`) for future reference.
- **User-Friendly Interface**: Simple console-based interface for encryption and decryption.

## How to Use

1. **Run the Program**:
   - Compile the `AES.java` file using `javac AES.java`.
   - Run the program using `java AES`.

2. **Choose an Option**:
   - **Option 1**: Encrypt a message.
     - Enter the message you want to encrypt.
     - The program will generate a new key and IV, encrypt the message, and save the encrypted data, key, and IV to `message.txt`.
   - **Option 2**: Decrypt a message.
     - Provide the Base64-encoded encrypted message, key, and IV.
     - The program will decrypt the message and display the original text.
   - **Option 3**: Exit the program.

3. **File Details**:
   - The `message.txt` file contains:
     - Encrypted Message
     - Key (Base64-encoded)
     - IV (Base64-encoded)
     - Date of encryption

## Example

### Encryption
- Input: `Hello, World!`
- Output:
  ```
  Encrypted Message: <Base64-encoded encrypted message>
  Message, Key, and IV saved to 'message.txt'.
  ```

### Decryption
- Input:
  - Encrypted Message: `<Base64-encoded encrypted message>`
  - Key: `<Base64-encoded key>`
  - IV: `<Base64-encoded IV>`
- Output:
  ```
  Decrypted Message: Hello, World!
  ```

## Error Handling

- **Invalid Base64 Input**: Displays an error if the provided key, IV, or encrypted message is not Base64-encoded.
- **Decryption Failure**: Displays an error if the decryption fails due to incorrect key, IV, or message.

## Dependencies

- Java Development Kit (JDK) 8 or higher.

## Notes

- Ensure the `message.txt` file is kept secure as it contains sensitive information (key and IV).
- The program uses AES with a 128-bit key and GCM mode for encryption.

## License

This project is provided as-is for educational purposes. Use it responsibly and ensure compliance with local laws and regulations regarding encryption.