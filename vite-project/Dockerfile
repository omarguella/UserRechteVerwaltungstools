FROM node:18
WORKDIR /app
COPY vite-project/package.json .
RUN npm install
# Build the app
COPY . .
EXPOSE 5173
CMD [ "npm", "run", "dev" ]

