import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IRecipeWithDetails, defaultValue } from 'app/shared/model/recipe-with-details.model';
import {IRecipe} from "app/shared/model/recipe.model";

const initialState: EntityState<IRecipeWithDetails> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

const apiUrl = 'api/recipes';

// Actions

export const getEntities = createAsyncThunk('recipeWithDetail/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}?cacheBuster=${new Date().getTime()}`;
  return axios.get<IRecipe[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'recipeWithDetail/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}/details`;
    return axios.get<IRecipeWithDetails>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'recipeWithDetail/create_entity',
  async (entity: IRecipeWithDetails, thunkAPI) => {
    const result = await axios.post<IRecipeWithDetails>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'recipeWithDetail/update_entity',
  async (entity: IRecipeWithDetails, thunkAPI) => {
    const result = await axios.put<IRecipeWithDetails>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const RecipeWithDetailsSlice = createEntitySlice({
  name: 'recipeWithDetail',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        const { data } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data,
        };
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      });
  },
});

export const { reset } = RecipeWithDetailsSlice.actions;

// Reducer
export default RecipeWithDetailsSlice.reducer;
