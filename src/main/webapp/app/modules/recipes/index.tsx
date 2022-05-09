import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import Recipes from "app/modules/recipes/recipes";
import RecipeWithDetails from "app/modules/recipes/recipe-with-details";
import {RecipeWithDetailsUpdate} from "app/modules/recipes/recipe-with-details-update";

/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RecipeWithDetailsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RecipeWithDetails} />
      <ErrorBoundaryRoute path={`${match.url}`} component={Recipes}  />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
