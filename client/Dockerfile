FROM node:20-alpine AS build
WORKDIR /app
COPY . /app
RUN npm install

EXPOSE 4200

CMD ["npm", "run", "dev"]