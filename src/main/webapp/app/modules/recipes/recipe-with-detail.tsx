import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './recipes.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RecipeWithDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const recipeEntity = useAppSelector(state => state.recipeWithDetail.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="recipeDetailsHeading">
          <Translate contentKey="cookbookApp.recipe.detail.title">Recipe</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">
              <Translate contentKey="cookbookApp.recipe.name">Name</Translate>
            </span>
          </dt>
          <dd>{recipeEntity.name}</dd>
          <dt>
            <span id="recipe-items">
              <Translate contentKey="cookbookApp.recipe.items">Items</Translate>
            </span>
          </dt>
          <dd>TODO</dd>
          <dt>
            <span id="description">
              <Translate contentKey="cookbookApp.recipe.description">Description</Translate>
            </span>
          </dt>
          <dd>{recipeEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/recipes" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/recipes/${recipeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RecipeWithDetail;
