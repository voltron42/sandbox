package framework

import (
	"github.com/gorilla/mux"
)

type Controller interface {
	Apply(r *mux.Router) error
}
