import { NgModule } from "@angular/core";
import { MatButtonModule } from '@angular/material/button'
import { MatIconModule } from '@angular/material/icon'
import { MatSnackBarModule } from '@angular/material/snack-bar'
import { MatDividerModule } from '@angular/material/divider'
import { MatInputModule } from '@angular/material/input'
import { MatDialogModule } from '@angular/material/dialog'
import { MatTableModule } from '@angular/material/table'
import { MatToolbarModule } from "@angular/material/toolbar"
import { MatTabsModule } from "@angular/material/tabs"
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';

const matModules: any[] = [
    MatButtonModule, MatIconModule,
    MatSnackBarModule, MatDividerModule, MatInputModule, MatDialogModule,
    MatTableModule, MatToolbarModule, MatTabsModule, MatProgressSpinnerModule
]

@NgModule({
    imports: matModules,
    exports: matModules
})

export class MaterialModule { }