package io.mtc.facade.user.util;

import io.mtc.common.constants.TransactionConstants;
import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

/**
 * 以太坊打包工具类
 *
 * @author Chinhin
 * 2018/7/31
 */
@Slf4j
public class PackageUtil {

    /***
     * 通过keystore打包eth转账交易
     */
    public static String packageEther(BigInteger[] gasPriceAndNonce, BigInteger amount, String to,
                                      String keyStorePath, String keyStorePassword) {
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                gasPriceAndNonce[1],
                TransactionConstants.getUseGasPrice(gasPriceAndNonce[0]),
                TransactionConstants.GAS_AMOUNT,
                to,
                amount);
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(keyStorePassword, keyStorePath);
        } catch (IOException | CipherException e) {
            log.error("签名失败 {} : {}", e.getMessage(), keyStorePath);
            e.printStackTrace();
        }
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        return Numeric.toHexString(signedMessage);
    }

    /***
     * 通过keystore打包ecr20代币转账交易
     */
    public static String packageCurrency(BigInteger[] gasPriceAndNonce, BigInteger amount, String to, String currencyAddress,
                                         String keyStorePath, String keyStorePassword) {
        RawTransaction rawTransaction = createTransaction(gasPriceAndNonce, amount, to, currencyAddress);
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(keyStorePassword, keyStorePath);
        } catch (IOException | CipherException e) {
            log.error("签名失败 {} : {}", e.getMessage(), keyStorePath);
            e.printStackTrace();
        }
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        return Numeric.toHexString(signedMessage);
    }

    /***
     * 通过privateKey打包eth转账交易
     */
    public static String packageEtherByPrivateKey(BigInteger[] gasPriceAndNonce, BigInteger amount, String to, String privateKey) {
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                gasPriceAndNonce[1],
                TransactionConstants.getUseGasPrice(gasPriceAndNonce[0]),
                TransactionConstants.GAS_AMOUNT,
                to,
                amount);
        Credentials credentials = Credentials.create(privateKey);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        return Numeric.toHexString(signedMessage);
    }

    /***
     * 通过privateKey打包ecr20代币转账交易
     */
    public static String packageCurrencyByPrivateKey(BigInteger[] gasPriceAndNonce, BigInteger amount, String to, String currencyAddress,
                                         String privateKey) {
        RawTransaction rawTransaction = createTransaction(gasPriceAndNonce, amount, to, currencyAddress);
        Credentials credentials = Credentials.create(privateKey);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        return Numeric.toHexString(signedMessage);
    }

    private static RawTransaction createTransaction(BigInteger[] gasPriceAndNonce, BigInteger amount, String to, String currencyAddress){
        Function function = new Function(
                "transfer",
                Arrays.asList(new Address(to), new Uint256(amount)),
                Collections.emptyList())
                ;
        String data = FunctionEncoder.encode(function);
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                gasPriceAndNonce[1],
                TransactionConstants.getUseGasPrice(gasPriceAndNonce[0]),
                TransactionConstants.GAS_AMOUNT,
                currencyAddress,
                data
        );
        return rawTransaction;
    }

}
