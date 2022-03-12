import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/recipe">
      <Translate contentKey="global.menu.entities.recipe" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/ingredient">
      <Translate contentKey="global.menu.entities.ingredient" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/unit">
      <Translate contentKey="global.menu.entities.unit" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/ingredient-for-recipe">
      <Translate contentKey="global.menu.entities.ingredientForRecipe" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
