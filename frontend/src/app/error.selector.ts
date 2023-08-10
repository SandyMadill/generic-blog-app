import { HttpErrorResponse } from "@angular/common/http";
import { createFeatureSelector } from "@ngrx/store";

export const selectError = createFeatureSelector<any>("error")