import { HttpClient, HttpParams } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { DataService } from '../data.service';

const url = '/swampfoxServiceLayer/public/demoCallback';

@Component({
  selector: 'app-confirmation-page',
  templateUrl: './confirmation-page.component.html',
  styleUrls: ['./confirmation-page.component.scss'],
})
export class ConfirmationPageComponent implements OnInit {
  constructor(private http: HttpClient, private service: DataService) {}

  phoneNumber: string = '';
  phoneParam: string = '';
  callTime: string = '';
  callSuccess: boolean = false;

  ngOnInit(): void {
    this.service.currentData.subscribe((data) => {
      console.log(data);
      this.phoneNumber = data;
    });

    this.setCallTime();
  }

  initiateCall() {
    this.callSuccess = true;

    this.phoneParam = 1 + this.phoneNumber.replace(/[^0-9]/gi, '');

    console.log(this.phoneParam);

    const options = this.phoneParam
      ? { params: new HttpParams().set('phoneNumber', this.phoneParam) }
      : {};

    this.http.post(url, {}, options).subscribe((response) => {
      console.log(response);
    });
  }

  setCallTime() {
    let currentTime = new Date();
    currentTime.setMinutes(currentTime.getMinutes() + 20);
    this.callTime = currentTime
      .toLocaleTimeString(navigator.language, {
        hour: '2-digit',
        minute: '2-digit',
      })
      .toLowerCase();
  }
}
