package com.example.fansonlib.base;

/**
 * Created by fansonq on 2017/9/2.
 * 数据层处理的BaseRepository
 */

public abstract class BaseRepository implements IBaseRepository {

    private static final String TAG = BaseRepository.class.getSimpleName();

    private int mCompositeDisposableType;

    public BaseRepository() {
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
