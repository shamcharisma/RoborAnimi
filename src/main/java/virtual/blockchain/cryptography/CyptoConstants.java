package virtual.blockchain.cryptography;

class CyptoConstants {
    static final int PRIVATE_KEY_BYTES = 20;
    static final int PUBLIC_KEY_BYTES = 65;
    static final int PRIVATE_KEY_SIZE = 256;
    static final String SECP256R1 = "secp256r1";
    static final String PRIME192V1 = "prime192v1";
    static final String ECDH = "ECDH";
    static final String BOUNCY_CASTLE = "BC";
    static final String EVEN_SIGN_BYTE = "02";
    static final String ODD_SIGN_BYTE = "03";
    static final String COMPRESSED_SIGN_BYTE = "04";

    static final String KEY_GENERATOR_ALGORITHM = "AES";
    static final String OPERATING_SYSTEM_NAME_ATTR = "os.name";
    static final String WINDOWS_PSEUDO_RANDOM_NUM_GENERATOR = "Windows-PRNG";
    static final String[] UNIX_FIX = {"nix", "nux", "rhel", "mac"};
    static final String[] WINDOWS_FIX = {"win"};
}
