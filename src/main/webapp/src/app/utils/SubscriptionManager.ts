import {Subscription} from "rxjs";

export class SubscriptionManager {
  private subscriptions: Array<Subscription> = [];

  constructor() {
  }

  add(subscription: Subscription): void {
    this.subscriptions.push(subscription);
  }

  clear(): void {
    this.unsubscribeAll();
    this.subscriptions = [];
  }

  unsubscribeAll(): void {
    for (let sub of this.subscriptions) {
      sub.unsubscribe();
    }
  }
}
