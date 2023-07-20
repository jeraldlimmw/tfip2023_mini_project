import { NgModule } from "@angular/core";
import { MaterialModule } from "./material.module";
import { MatIconRegistry } from "@angular/material/icon";
import { DomSanitizer, SafeResourceUrl } from "@angular/platform-browser";

@NgModule({
    imports: [MaterialModule]
})

export class IconModule {
    path = "assets/button-icons"

    constructor(
            private domSanitizer: DomSanitizer,
            private matIconRegistry: MatIconRegistry) {
        this.matIconRegistry
        .addSvgIcon('bin', this.setPath(`${this.path}/bin-svgrepo-com.svg`))
        .addSvgIcon('dollar', this.setPath(`${this.path}/dollar-sign-svgrepo-com (1).svg`))
        .addSvgIcon('share', this.setPath(`${this.path}/share-svgrepo-com (2).svg`))
        .addSvgIcon('email', this.setPath(`${this.path}/email-svgrepo-com (1).svg`))
        .addSvgIcon('camera', this.setPath(`${this.path}/camera-svgrepo-com.svg`))
        .addSvgIcon('percent', this.setPath(`${this.path}/percentage-svgrepo-com.svg`))
        .addSvgIcon('bar', this.setPath(`${this.path}/bar-chart-svgrepo-com.svg`))
    }

    private setPath(url: string): SafeResourceUrl {
        return this.domSanitizer.bypassSecurityTrustResourceUrl(url);
    }
}