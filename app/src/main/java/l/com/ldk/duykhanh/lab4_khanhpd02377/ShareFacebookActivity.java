package l.com.ldk.duykhanh.lab4_khanhpd02377;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import java.lang.annotation.Target;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import bolts.AppLink;

public class ShareFacebookActivity extends AppCompatActivity {

    Button btnShareLink, btnSharePhoto, btnShareVideo,btnShareVideo2;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    private ShareButton buttonShare;
    private static final int REQUEST_VIDEO_CODE = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_share_facebook);
        btnShareLink = findViewById(R.id.btnShareLink);
        btnSharePhoto = findViewById(R.id.btnSharePhoto);
        btnShareVideo = findViewById(R.id.btnShareVideo);
        btnShareVideo2 = findViewById(R.id.btnShareVideo2);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(ShareFacebookActivity.this, "Share successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(ShareFacebookActivity.this, "Share cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(ShareFacebookActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnShareLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setQuote("This is userful link")
                        .setContentUrl(Uri.parse("https://developers.facebook.com"))
                        .build();
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    shareDialog.show(content);
                }
            }
        });

        btnSharePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(image)
                        .setCaption("a")
                        .build();
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse("https://developers.facebook.com"))
                            .build();
                    shareDialog.show(linkContent);
                }


            }
        });


        btnShareVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select video"), REQUEST_VIDEO_CODE);
            }
        });

        btnShareVideo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri videoFileUri=Uri.parse("https://www.youtube.com/watch?v=zeLqNx7dMBM");
                ShareVideo shareVideo=new ShareVideo.Builder()
                        .setLocalUrl(videoFileUri)
                        .build();
                //There is no use by content
                ShareVideoContent content=new ShareVideoContent.Builder()
                        .setVideo(shareVideo)
                        .build();


            }
        });
    }

    public void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("edm.dev.androidfbshare", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == RESULT_OK) {

            if (requestCode == REQUEST_VIDEO_CODE) {

                Uri selectVideo = data.getData();

                ShareVideo video = new ShareVideo.Builder()
                        .setLocalUrl(selectVideo)
                        .build();

                ShareVideoContent shareVideoContent = new ShareVideoContent.Builder()
                        .setContentTitle("This is userful video")
                        .setContentDescription("Funny video from EDMT Dev download from Youtube")
                        .setVideo(video)
                        .build();
                shareDialog.show(shareVideoContent);



            }
        }
    }

}
