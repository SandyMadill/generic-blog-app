import { HttpClientModule } from "@angular/common/http";
import { NgModule } from "@angular/core";
import { EffectsModule } from "@ngrx/effects";
import * as subscriptionsReducer from "./subscriptions.reducer"
import { StoreModule } from "@ngrx/store";
import { SubscriptionsEffects } from "./subscriptions.effects";
import { SubscriptionsService } from "./subscriptions.service";

@NgModule({
    imports:[
        HttpClientModule,
        StoreModule.forFeature("subscriptions", subscriptionsReducer.getReducer),
        EffectsModule.forFeature(SubscriptionsEffects)
    ],
    providers:[
        SubscriptionsService
    ]
}) export class SubscriptionsModule{

}