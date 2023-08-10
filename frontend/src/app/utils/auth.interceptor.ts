import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpHandler, HttpRequest } from '@angular/common/http';
import * as authReducer from '../auth/auth.reducer'
import { Store } from '@ngrx/store';
import { selectAuthToken } from '../auth/auth.selector';

const TOKEN_HEADER_KEY = 'Authorization';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private readonly store: Store<authReducer.State> ) {
    this.token$.subscribe(token =>{
        if (!token){
            return
        }
        this.token = token
    })
  }
  token$ = this.store.select(selectAuthToken)
  token:string

  intercept(req: HttpRequest<any>, next: HttpHandler) { 
    let authReq = req;
    if (this.token != null) {
      authReq = req.clone({ headers: req.headers.set(TOKEN_HEADER_KEY, 'Bearer ' + this.token) });
    }
    return next.handle(authReq);
  }
}

export const authInterceptorProviders = [
  { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
];