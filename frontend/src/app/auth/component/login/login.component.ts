import { HttpClient } from '@angular/common/http';
import { Component, forwardRef } from '@angular/core';
import { Observable, combineLatest, map, take } from 'rxjs';
import * as authReducer from '../../auth.reducer'
import * as authActions from '../../auth.actions'
import { Store } from '@ngrx/store';
import { selectAuthLoggedIn, selectAuthState, selectAuthToken, selectAuthUser } from '../../auth.selector';;
import { User } from '../../../user/user.model';
import { FormBuilder, Validators, FormGroup, FormsModule, ReactiveFormsModule, NG_VALUE_ACCESSOR, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { MatFormField, MatFormFieldModule, MatFormFieldControl } from '@angular/material/form-field';
import { CommonModule } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { selectError } from 'src/app/error.selector';


@Component({
    selector: 'login-component',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']

})
export class LoginComponent {
  loggedIn$ = this.store.select(selectAuthLoggedIn)
  logInForm = this.formBuilder.group({
    username: ['', [Validators.required]],
    password: ['', [Validators.required]]
  })

  error$ = this.store.select(selectError)
  errorMessage:string

  constructor(private readonly store: Store<authReducer.State>, private readonly formBuilder: FormBuilder, private readonly router: Router) {
  }


  onSubmit(){
    if (this.logInForm.valid){
      const payload={
        username:this.logInForm.value.username,
        password:this.logInForm.value.password
      }
      this.store.dispatch(authActions.logInRequested({payload}))
    }
  }

  ngOnInit(){
    this.errorMessage=null
    this.loggedIn$.subscribe(loggedIn =>{
      if (loggedIn){
        this.router.navigate(["blogs/subscriptions"])
      }
    })

    this.error$.subscribe(error =>{
      if (error){  
        if(error?.status==401){
          this.errorMessage="*Username or password incorrect"
        }
        else if (error?.status)
        {
          this.errorMessage=`*${error?.message}`
        }
      }
    })
  }
}