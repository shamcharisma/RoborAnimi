package virtual.blockchain.cryptography;

import lombok.Builder;

@Builder
public class EllipticCurveXYBytes {
    byte[] xCoordBytes;
    byte[] yCoordBytes;
}
