import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Recipe from './recipe';
import Ingredient from './ingredient';
import Unit from './unit';
import IngredientForRecipe from './ingredient-for-recipe';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}recipe`} component={Recipe} />
      <ErrorBoundaryRoute path={`${match.url}ingredient`} component={Ingredient} />
      <ErrorBoundaryRoute path={`${match.url}unit`} component={Unit} />
      <ErrorBoundaryRoute path={`${match.url}ingredient-for-recipe`} component={IngredientForRecipe} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
