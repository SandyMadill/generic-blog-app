import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule, HttpErrorResponse } from '@angular/common/http'
import { ActionReducer, ActionReducerMap, MetaReducer, Store, StoreModule, createFeature, createFeatureSelector, createSelector } from '@ngrx/store';
import { storageSync } from '@larscom/ngrx-store-storagesync';
import { EffectsModule } from '@ngrx/effects';
import { AuthInterceptor, authInterceptorProviders } from './utils/auth.interceptor';
import { AuthModule } from './auth/auth.module';
import { NavigationComponent } from './navigation/navigation.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatCardModule } from '@angular/material/card';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { CommonModule } from '@angular/common';
import { BlogsModule } from './blogs/blogs.module';
import { IntersectionDirective } from './app-intersection/app-intersection.directive';
import { selectAuthToken } from './auth/auth.selector';
import { UserModule } from './user/user.module';
import { SubscriptionsModule } from './subscriptions/subscriptions.module';

export interface State {
  error:Error|HttpErrorResponse
}

export const reducers: ActionReducerMap<State> = { 
  error:undefined
};


export function syncReducer(reducer: ActionReducer<any>){
  return function (state, action){
    console.log(state)
    console.log(action)
    if (action?.error){
      state.error=action.error
    }
    return reducer(state, action)
  }
}

export function storageSyncReducer(reducer: ActionReducer<any>): ActionReducer<any> {
  return storageSync<State>({
    features: [
      { stateKey: 'auth'}
    ],
    storage: window.sessionStorage
  })(reducer);
}

export const metaReducers: Array<MetaReducer<any, any>> = [storageSyncReducer, syncReducer];
@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    
  ],
  imports: [
    CommonModule,
    HttpClientModule,
    BrowserModule,
    AppRoutingModule,
    StoreModule.forRoot(reducers, {metaReducers}),
    StoreModule.forFeature("error",reducers),
    EffectsModule.forRoot([]),
    AuthModule,
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    MatIconModule,
    MatListModule,
    BrowserAnimationsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    BlogsModule,
    UserModule,
    SubscriptionsModule
  ],
  providers: [
    authInterceptorProviders
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
