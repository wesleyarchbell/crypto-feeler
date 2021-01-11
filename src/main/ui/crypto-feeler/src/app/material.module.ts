import { NgModule } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
  imports: [
    BrowserAnimationsModule,
    MatToolbarModule
  ],
  exports: [
    BrowserAnimationsModule,
    MatToolbarModule
  ]
})
export class MaterialModule { }
