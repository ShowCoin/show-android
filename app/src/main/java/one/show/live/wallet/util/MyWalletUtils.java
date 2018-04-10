package one.show.live.wallet.util;


import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;

import static org.web3j.crypto.Hash.sha256;
import static org.web3j.crypto.WalletUtils.generateWalletFile;

/**
 * Created by samuel on 2018/3/20.
 */

public class MyWalletUtils {

    private static final SecureRandom secureRandom = SecureRandomUtils.secureRandom();

    /**
     * 生成钱包keystore文件，并导出助记词或者私钥
     *
     * @param password
     * @param destinationDirectory
     * @return
     * @throws CipherException
     * @throws IOException
     */
    public static Bip39Wallet generateBip39Wallet(String password, File destinationDirectory)
            throws CipherException, IOException {
        byte[] initialEntropy = new byte[16];
        secureRandom.nextBytes(initialEntropy);

        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        ECKeyPair privateKey = ECKeyPair.create(sha256(seed));

        String walletFile = generateWalletFile(password, privateKey, destinationDirectory, false);

        return new Bip39Wallet(walletFile, mnemonic);
    }

    /**
     * 通过助记词恢复钱包
     * @param password
     * @param mnemonic
     * @return
     */
    public static Credentials loadBip39Credentials(String password, String mnemonic) {
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        return Credentials.create(ECKeyPair.create(sha256(seed)));
    }
}
