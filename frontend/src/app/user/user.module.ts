import { NgModule } from '@angular/core';
import { UserComponent } from './component/user.component';
import { StoreModule } from '@ngrx/store';
import * as userReducer from './user.reducer';
import { EffectsModule } from '@ngrx/effects';
import { UserEffects } from './user.effects';
import { UserService } from './user.service';
import { HttpClientModule } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from '../app-routing.module';
import { UserListComponent } from './component/user-list/user-list.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { UserSettingsComponent } from './component/user-settings/user-settings.component';

@NgModule({
    declarations:[
        UserComponent,
        UserListComponent,
        UserSettingsComponent
    ],
    imports:[
        HttpClientModule,
        BrowserModule,
        AppRoutingModule,
        StoreModule.forFeature("user", userReducer.getReducer),
        EffectsModule.forFeature(UserEffects),
        MatProgressSpinnerModule,
        
    ],
    providers:
    [
        UserService
    ],
    bootstrap:
    [
        UserComponent
    ]
}) export class UserModule{

}