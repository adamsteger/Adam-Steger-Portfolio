import { Component } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { DataService } from '../data.service';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.scss'],
})
export class FormComponent {
  constructor(private service: DataService) {}

  callTime: string = '';
  phoneNumber: string = '';

  startConfirmation() {
    this.service.setData(this.phoneNumber);
  }
}
