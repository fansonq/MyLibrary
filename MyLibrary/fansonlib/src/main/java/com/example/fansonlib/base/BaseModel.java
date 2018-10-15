package com.example.fansonlib.base;

/**
 * @author Created by：Fanson
 * Created Time: 2017/9/2.
 * Describe：数据层操作的BaseModel
 */
public class BaseModel implements IBaseRepository {

    private static final String TAG = BaseModel.class.getSimpleName();

    private int mCompositeDisposableType;

    public BaseModel() {
    }

    /**
     * RxJava取消注册，以避免内存泄露
     */
    protected void unSubscribe() {
//        HttpUtils.getHttpUtils().cancelAll();
    }


    /* protected void addSubscrebe(Disposable disposable) {
         if (mCompositeDisposable == null) {
             mCompositeDisposable = new CompositeDisposable();
         }
         mCompositeDisposable.add(disposable);
     }
 */
//    protected ResourceSubscriber addSubscrebe(Flowable observable, ResourceSubscriber subscriber) {
//        return (ResourceSubscriber) observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnLifecycle(new Consumer<Subscription>() {
//                    @Override
//                    public void accept(Subscription subscription) throws Exception {
//                        Log.d(TAG, "OnSubscribe");
//                    }
//                }, new LongConsumer() {
//                    @Override
//                    public void accept(long t) throws Exception {
//                        Log.d(TAG, "OnRequest");
//                    }
//                }, new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        Log.d(TAG, "OnCancel");
//                    }
//                })
//                .subscribeWith(subscriber);
//    }

    @Override
    public void onDestroy() {
        unSubscribe();
    }

}
