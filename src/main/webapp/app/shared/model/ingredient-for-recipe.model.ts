import { IIngredient } from 'app/shared/model/ingredient.model';
import { IUnit } from 'app/shared/model/unit.model';
import { IRecipe } from 'app/shared/model/recipe.model';

export interface IIngredientForRecipe {
  id?: number;
  quantity?: number;
  ingredient?: IIngredient;
  unit?: IUnit;
  recipe?: IRecipe;
}

export const defaultValue: Readonly<IIngredientForRecipe> = {};
