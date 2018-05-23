package virtual.blockchain.cryptography;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

/*
TODO: Need to add chi-square testing or other diehard random number testing mechanism
 */
public class PrivateKeyGeneratorTest {
    private PrivateKeyGenerator privateKeyGenerator;

    @Before
    public void setup() {
        privateKeyGenerator = new PrivateKeyGenerator();
    }

    @Test
    public void very_simple_randomness_check_for_private_key_generator_to_test_unique_private_keys() {
        Consumer<List<String>> privateKeyConsumer = System.out::println;

        final List<String> private1000Keys = getNPrivateKeys(1_000);
        privateKeyConsumer.accept(private1000Keys);
        verifyIfUniqueKeys(private1000Keys);

        final List<String> private10000Keys = getNPrivateKeys(10_000);
        privateKeyConsumer.accept(private10000Keys);
        verifyIfUniqueKeys(private10000Keys);
    }

    private void verifyIfUniqueKeys(List<String> randList) {
        randList.forEach(x -> {
            int currentIndex = randList.indexOf(x);
            if (currentIndex < randList.size() - 1) {
                assertTrue(!x.equals(randList.get(currentIndex + 1)));
            }
        });
    }

    private String getPrivateKey() {
        return PrivateKeyGenerator.convertPrivateKeyToHex(privateKeyGenerator.generatePrivateKey().getEncoded());
    }

    private List<String> getNPrivateKeys(int n) {
        return IntStream.range(0, n)
                .mapToObj(x -> getPrivateKey())
                .collect(Collectors.toList());
    }
}
