package one.show.live.wallet.ui;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import one.show.live.R;
import one.show.live.widget.TitleView;
import one.show.live.common.ui.BaseFragment;

/**
 * "我"的页面
 */

public class MeFragment extends BaseFragment {
    @BindView(R.id.main_wallet)
    LinearLayout wallet;
    @BindView(R.id.main_title)
    TitleView mainTitle;
    @BindView(R.id.main_qr)
    ImageView mainQr;

    public static MeFragment newInstance(){
        MeFragment meFragment = new MeFragment();
        return meFragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initView() {
        super.initView();

        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(WalletActivity.getCallingIntent(getContext()));
            }
        });
        mainQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(QrCodeActivity.getCallingIntent(getContext()));
            }
        });

        mainTitle.setTitle(getString(R.string.me)).setTextColor(ContextCompat.getColor(getContext(), R.color.color_333333));
        mainTitle.setLayBac(R.color.color_ffffff);





        //  TODO  test


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    String url = "http://39.107.241.250:9485";
//                    Web3j web3 = Web3jFactory.build(new HttpService(url));
//                    Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().sendAsync().get();
//                    String clientVersion = web3ClientVersion.getWeb3ClientVersion();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//
//
//                    // 获取凭证
//                    int REQUEST_EXTERNAL_STORAGE = 1;
//                    String[] PERMISSIONS_STORAGE = {
//                            Manifest.permission.READ_EXTERNAL_STORAGE,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE
//                    };
//                    int permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//                    if (permission != PackageManager.PERMISSION_GRANTED) {
//                        // We don't have permission so prompt the user
//                        ActivityCompat.requestPermissions(
//                                MainActivity.this,
//                                PERMISSIONS_STORAGE,
//                                REQUEST_EXTERNAL_STORAGE
//                        );
//                    }
//                    String filePath = Environment.getExternalStorageDirectory().toString() + "/Pictures";
////                    String fileName = WalletUtils.generateNewWalletFile("123456",new File(filePath),false);
//
//                    Bip39Wallet bit39wallet = MyWalletUtils.generateBip39Wallet("123456", new File(filePath));
//
//                    Credentials credentials = WalletUtils.loadCredentials("123456", filePath + "/" + bit39wallet.getFilename());
//
//
////                    WalletUtils.generateBip39Wallet("123456"," ");
//                    final String myAddress = credentials.getAddress();
//
//                    final String mnemonics = bit39wallet.getMnemonic();
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mainTitle.setTitle(mnemonics);
//                        }
//                    });
//
//                } catch (IOException e){
//                    e.printStackTrace();
//                }catch (CipherException e){
//                    e.printStackTrace();
//                }
////                catch (NoSuchAlgorithmException e) {
////                    e.printStackTrace();
////                } catch (NoSuchProviderException e) {
////                    e.printStackTrace();
////                } catch (InvalidAlgorithmParameterException e) {
////                    e.printStackTrace();
////                }
//            }
//        }).start();

    }


}
