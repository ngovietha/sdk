/*
 * Created by NQC on 4/26/20 2:41 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */
package com.nqc.idoctor.common.utils;


import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import timber.log.Timber;

/**
 * A lifecycle-aware observable that sends only new updates after subscription, used for events like navigation and
 * Snack bar messages.
 * <p>
 * This avoids a common problem with events: on configuration change (like rotation) an update can be emitted if the
 * observer is active. This LiveData only calls the observable if there's an explicit call to setValue() or call().
 * <p>
 * Note that only one observer is going to be notified of changes.
 */
public class SingleLiveEvent<T> extends MutableLiveData<T> {

  private final AtomicBoolean mPending = new AtomicBoolean(false);


  @MainThread
  public void observe(@NotNull LifecycleOwner owner, @NotNull final Observer<? super T> observer) {
    if (hasActiveObservers()) {
      Timber.w("Multiple observers registered but only one will be notified of changes.");
    }
    // Observe the internal MutableLiveData
    super.observe(owner, t -> {
      if (mPending.compareAndSet(true, false)) {
        observer.onChanged(t);
      }
    });
  }

  @MainThread
  public void setValue(@Nullable T t) {
    mPending.set(true);
    super.setValue(t);
  }

  /**
   * Used for cases where T is Void, to make calls cleaner.
   */
  @MainThread
  public void call() {
    setValue(null);
  }
}