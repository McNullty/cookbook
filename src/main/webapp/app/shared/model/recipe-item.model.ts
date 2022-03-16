
export interface IRecipeItem {
  ingredient?: string;
  quantity?: number;
  unit?: string;
}

export const defaultValue: Readonly<IRecipeItem> = {};
