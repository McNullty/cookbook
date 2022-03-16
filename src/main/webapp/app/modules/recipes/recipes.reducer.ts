import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IRecipeWithDetails, defaultValue } from 'app/shared/model/recipe-with-details.model';

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


export const getEntity = createAsyncThunk(
  'recipe/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}/details`;
    return axios.get<IRecipeWithDetails>(requestUrl);
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
      .addMatcher(isPending(getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
  },
});

export const { reset } = RecipeWithDetailsSlice.actions;

// Reducer
export default RecipeWithDetailsSlice.reducer;
