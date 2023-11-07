import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FormComponent } from './form/form.component';
import { ConfirmationPageComponent } from './confirmation-page/confirmation-page.component';

const routes: Routes = [
  { path: '', component: FormComponent },
  { path: 'confirmation', component: ConfirmationPageComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
