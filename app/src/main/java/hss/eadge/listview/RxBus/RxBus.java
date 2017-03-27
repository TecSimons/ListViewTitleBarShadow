package hss.eadge.listview.RxBus;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * 用来替代EventBus
 *
 * Created by Siy on 2016/8/24
 */
public class RxBus {

    //如果多个线程将发出事件给这个，那么它必须使线程安全,像这样（Subject是线程不安全的）
    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());

    public void send(Object o) {
        _bus.onNext(o);
    }

    public Observable<Object> toObserverable() {
        return _bus;
    }

    public boolean hasObservers() {
        return _bus.hasObservers();
    }
}