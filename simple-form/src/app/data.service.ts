import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
@Injectable({
  providedIn: 'root',
})
export class DataService {
  private data = new BehaviorSubject('');
  currentData = this.data.asObservable();

  constructor() {}

  setData(data: string) {
    this.data.next(data);
  }
}
