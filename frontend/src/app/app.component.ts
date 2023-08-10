import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Observable, combineLatest, filter, map } from 'rxjs';
import * as authReducer from './auth/auth.reducer'
import * as authActions from './auth/auth.actions'
import { Store } from '@ngrx/store';
import { selectAuthLoggedIn, selectAuthState, selectAuthToken, selectAuthUser } from './auth/auth.selector';
import { LocalService } from './local.service';
import { User } from './user/user.model';
import { NavigationStart, Router, RouterEvent } from '@angular/router';
import { State } from './app.module';
import { getSubscriptionsRequested } from './subscriptions/subscriptions.actions';
import { Title } from '@angular/platform-browser';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  constructor(private http:HttpClient, private readonly store: Store<State>, private readonly localService: LocalService, private readonly router:Router, private readonly title:Title) {
  }

  ngOnInit(){
    this.title.setTitle("Generic Blog App")
    this.router.events.pipe(
      filter(event => event instanceof(NavigationStart))
    ).subscribe(()=>{
      this.store.pipe(map(state => state.error=null))
      this.title.setTitle("Generic Blog App")
    })
    combineLatest([this.store.select(selectAuthLoggedIn), this.store.select(selectAuthUser)]).subscribe(([loggedIn,user]) =>{
      if(loggedIn && !user){
        this.store.dispatch(authActions.getLoggedInUserRequested())
      }
      else if (loggedIn)
      {
        this.store.dispatch(getSubscriptionsRequested())
      }
    })

    
  }
}
