import { storageSync } from "@larscom/ngrx-store-storagesync";
import { ActionReducer, ActionReducerMap, MetaReducer, StoreModule } from "@ngrx/store";
import { HttpClientModule } from "@angular/common/http";
import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { EffectsModule } from "@ngrx/effects";
import { AppRoutingModule } from "../app-routing.module";
import { AppComponent } from "../app.component";
import { State, reducers } from "../app.module";
import { authInterceptorProviders } from "../utils/auth.interceptor";
import { AuthEffects } from "./auth.effect";
import { AuthService } from "./auth.service";
import * as authReducer from "./auth.reducer"
import { LoginComponent } from "./component/login/login.component";
import { FormsModule, ReactiveFormsModule } from '@angular/forms'
import { LogoutComponent } from "./component/logout/logout.component";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input"
import { CommonModule } from "@angular/common";
import { MatCardModule } from "@angular/material/card";
import { RegisterComponent } from "./component/register/register.component";

  @NgModule({
    declarations:[
        LoginComponent,
        LogoutComponent,
        RegisterComponent
    ],
    imports: [
        CommonModule,
        HttpClientModule,
        BrowserModule,
        FormsModule,
        ReactiveFormsModule,
        StoreModule.forFeature("auth", authReducer.reducer),
        EffectsModule.forFeature([AuthEffects]),
        MatCardModule,
        MatFormFieldModule,
        MatInputModule
    ],
    exports:[
      MatFormFieldModule,
        MatInputModule
    ],
    providers: [
        AuthService,
        authInterceptorProviders
    ],
    bootstrap: [AppComponent]
})
  export class AuthModule { }