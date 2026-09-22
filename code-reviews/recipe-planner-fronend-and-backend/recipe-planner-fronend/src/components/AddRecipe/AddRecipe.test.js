import React from 'react';
import {fireEvent, render, screen, waitFor} from '@testing-library/react';
import {MemoryRouter, Route, Routes} from 'react-router-dom';
import axios from 'axios';
import AddRecipe from './AddRecipe';

jest.mock('axios');

describe('AddRecipe', () => {
    it('creates a new recipe on submit', async () => {
        axios.post.mockResolvedValue({data: {}});

        render(
            <MemoryRouter>
                <AddRecipe/>
            </MemoryRouter>
        );

        fireEvent.change(screen.getByLabelText(/Recipe Name/), {target: {value: 'Spaghetti Carbonara'}});
        fireEvent.click(screen.getByRole('button', {name: /submit/i}));

        await waitFor(() => expect(axios.post).toHaveBeenCalledWith(
            'http://localhost:8080/api/recipes',
            expect.objectContaining({name: 'Spaghetti Carbonara', ingredients: []})
        ));
    });

    it('prefills the form and updates the recipe on submit', async () => {
        axios.get.mockResolvedValue({data: {
            id: 'recipe-1',
            name: 'Old name',
            description: 'Old description',
            imageUrl: 'http://localhost/old.jpg',
            ingredients: [{id: 'ingredient-1', name: 'Tomato', comment: '', unit: 'PIECE', amount: 2}],
        }});
        axios.put.mockResolvedValue({data: {}});

        render(
            <MemoryRouter initialEntries={['/edit-recipe/recipe-1']}>
                <Routes>
                    <Route path="/edit-recipe/:id" element={<AddRecipe/>}/>
                </Routes>
            </MemoryRouter>
        );

        await waitFor(() => expect(screen.getByLabelText(/Recipe Name/).value).toBe('Old name'));
        expect(screen.getAllByPlaceholderText('Name').find(input => input.value === 'Tomato').value).toBe('Tomato');

        fireEvent.change(screen.getByLabelText(/Recipe Name/), {target: {value: 'New name'}});
        fireEvent.click(screen.getByRole('button', {name: /submit/i}));

        await waitFor(() => expect(axios.put).toHaveBeenCalledWith(
            'http://localhost:8080/api/recipes/recipe/recipe-1',
            expect.objectContaining({
                name: 'New name',
                ingredients: expect.arrayContaining([
                    expect.objectContaining({id: 'ingredient-1', name: 'Tomato', amount: 2}),
                ]),
            })
        ));
    });
});