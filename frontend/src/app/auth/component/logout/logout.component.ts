import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Observable, combineLatest, map } from 'rxjs';
import * as authReducer from '../../auth.reducer'
import * as authActions from '../../auth.actions'
import { Store } from '@ngrx/store';
import { selectAuthLoggedIn, selectAuthState, selectAuthToken, selectAuthUser } from '../../auth.selector';;
import { User } from '../../../user/user.model';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { storageSync } from '@larscom/ngrx-store-storagesync';
import { metaReducers } from 'src/app/app.module';

@Component({
    selector: 'logout-component',
    templateUrl: './logout.component.html',
    styleUrls: ['./logout.component.scss']
})
export class LogoutComponent {
  authState$ = this.store.select(selectAuthState)
  title = 'logout';

  constructor(private readonly store: Store<authReducer.State>, private readonly router: Router) {
  }

  ngOnInit(){

    this.authState$.subscribe(authState => {
      if (authState.loggedIn){
        this.store.dispatch(authActions.logOutRequested())
      }
      else if(!authState.token && !authState.userData){
        this.router.navigate(['blogs/latest']).then(() => {
          window.location.reload();
        });
      }
    })
  }
}