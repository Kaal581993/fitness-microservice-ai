// MongoDB initialization script
// This script runs when the MongoDB container starts for the first time

// Switch to the aiactivityfitness database
db = db.getSiblingDB('aiactivityfitness');

// Create a user with readWrite permissions on the aiactivityfitness database
db.createUser({
  user: 'fitnessuser',
  pwd: 'fitnesspass123',
  roles: [
    { role: 'readWrite', db: 'aiactivityfitness' },
    { role: 'dbAdmin', db: 'aiactivityfitness' }
  ]
});

// Create the activities collection (optional, MongoDB creates it automatically on first insert)
db.createCollection('activities');

print('MongoDB initialization completed: User created for aiactivityfitness database');
