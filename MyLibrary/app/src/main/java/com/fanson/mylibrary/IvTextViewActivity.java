package com.fanson.mylibrary;

import com.example.fansonlib.base.BaseActivity;
import com.example.fansonlib.image.ImageLoaderConfig;
import com.example.fansonlib.image.ImageLoaderUtils;
import com.example.fansonlib.image.glide.GlideLoaderStrategy;
import com.example.fansonlib.utils.StringUtils;
import com.example.fansonlib.widget.customeditor.IvTextView;

/**
 * @author Created by：fanson
 *         Created Time: 2018/4/26 15:44
 *         Describe：测试图文控件IvTextView
 */

public class IvTextViewActivity extends BaseActivity {

    public static final String BASE_URL = "http://119.28.15.112:8081";
    private IvTextView mIvTextView;
    private String content1 = "死了 278 次的比特幣，為何每次都可以強勢回歸？\n" +
            "【我為什麼挑選這篇文章】沒有人能預測下一次比特幣暴跌暴漲是什麼時候，過去比特幣不斷被宣告死亡，但總是強勢回歸；比特幣跟過去的「經濟泡沫」背景不一樣，相反地，比特幣的資產因為「泡沫」而被證明在市場上越來越有價值。（責任編輯：24占）\n" +
            "\n" +
            "隨著比特幣價格的起起伏伏，媒體一直孜孜不倦地報導著比特幣的「死亡」。根據 99bitcoins 網站實時更新的「比特幣訃告」，到目前為止，比特幣已「死亡」278 次。其中，2011 年 1 月至 6 月，被報導 3 次「死亡」； 2013 年 10 月至 11 月有 5 次；2018 年以來有 46 次。";

    private String content2 =  "<img src=\"/file//images/article//2/u2KWs_1523945230506284.png\"/>";

    private String content3 = "测试图片中插入文字";

    private String content4 = "<img src=\"/file//images/article//31/YwRBX_1524564495038104.jpg\"/>";

    private String content5 = "<img src=\"/file//images/article//3/6AZnj_1524565337661150.jpg\"/>";

    private String content6 = "這真的只是巧合嗎？也許是。\n" +
            "\n" +
            "比特幣需求的激增不僅加劇了泡沫，也觸發了對期貨市場的需求。但是，比特幣價格崩盤的時機恰好發生在引入比特幣期貨市場之後，這正是其可怕之處。它反映了經濟學家 Charles Noussair 和 Steven Tucker 在 2006 年發表的一篇論文的研究結果，論文將期貨市場引入交易實驗：";

    @Override
    protected int getContentView() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView() {
        mIvTextView = findMyViewId(R.id.ivText);
    }

    @Override
    protected void initData() {
        initImageUtil();
        setData(content1);
        setData(content2);
        setData(content3);
        setData(content4);
        setData(content5);
        setData(content6);
    }

    private void initImageUtil() {
        //图片框架使用方式，策略模式
        ImageLoaderConfig loaderConfig = new ImageLoaderConfig.Builder().client(new GlideLoaderStrategy())
                .placePicRes(R.mipmap.default_image)
                .errorPicRes(R.mipmap.default_image)
                .setMaxDiskCache(1024 * 1024 * 50)
                .setMaxMemoryCache(1024 * 1024 * 10)
                .build();
        ImageLoaderUtils.init(loaderConfig);
    }

    private void setData(String text) {
        if (text.contains("<img") && text.contains("src=")) {
            String imagePath = BASE_URL + StringUtils.getImgSrc(text);
            mIvTextView.addImageViewAtIndex(mIvTextView.getLastIndex(), imagePath);
        } else {
            mIvTextView.addTextViewAtIndex(mIvTextView.getLastIndex(), text);
        }
    }

    @Override
    protected void listenEvent() {

    }
}
