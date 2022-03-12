import { IIngredientForRecipe } from 'app/shared/model/ingredient-for-recipe.model';

export interface IRecipe {
  id?: number;
  name?: string;
  description?: string | null;
  ingredientForRecipes?: IIngredientForRecipe[] | null;
}

export const defaultValue: Readonly<IRecipe> = {};
