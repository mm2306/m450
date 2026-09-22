import './Recipe.css'
import {Button, Card} from "react-bootstrap";
import React from "react";
import {useNavigate} from "react-router-dom";

function Recipe({recipe}) {
    const navigate = useNavigate();
    return (
        <Card style={{width: '18rem'}}>
            <Card.Img variant="top" src={recipe.imageUrl}/>
            <Card.Body>
                <Card.Title>{recipe.name}</Card.Title>
                {recipe.description}
                <Button variant="primary" onClick={() => navigate(`/edit-recipe/${recipe.id}`)}>Edit Details</Button>
            </Card.Body>
        </Card>
    )
}

export default Recipe;