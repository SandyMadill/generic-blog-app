import { HttpClient } from '@angular/common/http';
import { Component, forwardRef } from '@angular/core';
import { Observable, combineLatest, filter, map, take } from 'rxjs';
import * as authReducer from '../../auth.reducer'
import * as authActions from '../../auth.actions'
import { ActionReducer, ActionsSubject, Store, on } from '@ngrx/store';
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
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.scss']

})
export class RegisterComponent {
  loggedIn$ = this.store.select(selectAuthLoggedIn)
  username:string
  password:string
  registerForm = this.formBuilder.group({
    username: ['', [Validators.required]],
    displayName: ['', Validators.required],
    password: ['', [Validators.required]],
    confirmPassword: ['', Validators.required]
  })
  error$ = this.store.select(selectError)
  errorMessage:string

  constructor(private readonly store: Store<authReducer.State>, private readonly formBuilder: FormBuilder, private readonly router: Router,private readonly action:ActionsSubject) {
  }


  onSubmit(){
    this.errorMessage=null
    if (this.registerForm.valid){
      if(this.registerForm.controls.password.value == this.registerForm.controls.confirmPassword.value){
        const payload={
          username:this.registerForm.value.username,
          displayName:this.registerForm.value.displayName,
          password:this.registerForm.value.password
        }
        this.username=payload.username,
        this.password=payload.password
        this.store.dispatch(authActions.registerUserRequested({payload}))
      }
      else{
        this.errorMessage="*Passwords don't match"
      }
    }

  }

  ngOnInit(){
    this.loggedIn$.subscribe(loggedIn =>{
      if (loggedIn){
        this.router.navigate(["blogs/subscriptions"])
      }
    })

    this.action.subscribe(action =>{
      if(action.type==authActions.registerUserSucceeded.type){
        this.store.dispatch(authActions.logInRequested({payload:{username:this.username, password:this.password}}))
      }
      
    })

    this.error$.subscribe(error =>{
      if (error){
        if(error?.status==500){
          this.errorMessage="This username already exists"
        }
        else if (error?.status)
        {
          this.errorMessage=`*${error?.message}`
        }
      }
    })
  }
}