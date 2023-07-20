import { Component, Inject, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-email-dialog',
  templateUrl: './email-dialog.component.html',
  styleUrls: ['./email-dialog.component.css']
})
export class EmailDialogComponent implements OnInit{
  form!: FormGroup
  email!: string
  
  fb = inject(FormBuilder)

  constructor(
    private dialogRef: MatDialogRef<EmailDialogComponent>, 
    @Inject(MAT_DIALOG_DATA) data: any) {
      this.email = data.email
  }

  ngOnInit(): void {
      this.form = this.fb.group({
        email: this.fb.control<string>('', [ Validators.email ])
      })
  }

  saveEmail() {
    this.dialogRef.close(this.form.value);
  }

  close(): void {
    this.dialogRef.close();
  }
}
