package virtual.blockchain.cryptography;

import org.junit.Before;
import org.junit.Test;

import java.security.interfaces.ECPrivateKey;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PublicKeyGeneratorTest {

    private PrivateKeyGenerator privateKeyGenerator;
    private PublicKeyGenerator publicKeyGenerator;

    @Before
    public void setup() {
        privateKeyGenerator = new PrivateKeyGenerator();
        publicKeyGenerator = new PublicKeyGenerator(privateKeyGenerator.generatePrivateKey());
    }
    
    @Test
    public void verify_if_same_public_key_is_created_for_given_private_key() {
        final ECPrivateKey privateKey = privateKeyGenerator.generatePrivateKey();

        System.out.println("Private key: " + privateKey);

        final String firstTimeGeneratedPublicKey = publicKeyGenerator.getPublicKey();
        final String secondTimeGeneratedPublicKey = publicKeyGenerator.getPublicKey();

        System.out.println(firstTimeGeneratedPublicKey);
        System.out.println(secondTimeGeneratedPublicKey);

        assertEquals(firstTimeGeneratedPublicKey, secondTimeGeneratedPublicKey);
    }

    @Test
    public void verify_if_same_public_key_is_created_for_very_large_num_of_public_key_generation_given_same_private_key() {
        Consumer<List<String>> publicKeyConsumer = System.out::println;
        final ECPrivateKey privateKey = privateKeyGenerator.generatePrivateKey();

        final List<String> private1000Keys = getNPublicKeys(1_000);
        publicKeyConsumer.accept(private1000Keys);
        verifyIfSameKeys(private1000Keys);

        final List<String> private10000Keys = getNPublicKeys(10_000);
        publicKeyConsumer.accept(private10000Keys);
        verifyIfSameKeys(private10000Keys);
    }

    private void verifyIfSameKeys(List<String> randList) {
        randList.forEach(x -> {
            int currentIndex = randList.indexOf(x);
            if (currentIndex < randList.size() - 1) {
                assertTrue(x.equals(randList.get(currentIndex + 1)));
            }
        });
    }

    private List<String> getNPublicKeys(int n) {
        return IntStream.range(0, n)
                .mapToObj(x -> publicKeyGenerator.getPublicKey())
                .collect(Collectors.toList());
    }
}
