import {IRecipeItem} from "app/shared/model/recipe-item.model";

export interface IRecipeWithDetails {
  id?: number;
  name?: string;
  description?: string | null;
  recipeItems?: IRecipeItem[] | null;
}

export const defaultValue: Readonly<IRecipeWithDetails> = {};
